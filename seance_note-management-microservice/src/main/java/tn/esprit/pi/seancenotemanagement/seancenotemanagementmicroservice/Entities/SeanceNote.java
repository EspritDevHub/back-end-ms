package tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Entities;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import tn.esprit.pi.seancenotemanagement.seancenotemanagementmicroservice.Enum.TypeNote;
import java.util.Date;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document("seances")
public class SeanceNote {
    @Id
    private String id;
    private String titre;
    private String description;
    private Long numero ;
    private Double note ;
    private String sprintId;
    private List<String> critereIds;
    private Date date;
    private TypeNote typeNote;

}