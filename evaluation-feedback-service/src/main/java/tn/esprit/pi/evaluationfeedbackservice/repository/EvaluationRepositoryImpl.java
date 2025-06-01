package tn.esprit.pi.evaluationfeedbackservice.repository;

import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;

public class EvaluationRepositoryImpl implements EvaluationRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    public EvaluationRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Double averageNoteByProjet(Long projetId) {
        MatchOperation match = Aggregation.match(Criteria.where("projet").is(projetId));
        GroupOperation group = Aggregation.group().avg("note").as("averageNote");
        Aggregation aggregation = Aggregation.newAggregation(match, group);

        AggregationResults<Document> results = mongoTemplate.aggregate(aggregation, "evaluation", Document.class);
        Document result = results.getUniqueMappedResult();
        if (result != null) {
            return result.getDouble("averageNote");
        }
        return null;
    }
}

