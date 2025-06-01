from pymongo import MongoClient
from bson.objectid import ObjectId

def process_data():
    client = MongoClient("mongodb://localhost:27017/")

    # Bases et collections
    note_db = client["note-service-db"]
    seance_db = client["seance-service-db"]

    notes_collection = note_db["notes"]
    seances_collection = seance_db["seances"]
    output_collection = note_db["note_seance"]

    joined_data = []

    notes = list(notes_collection.find())
    print(f"Nombre de notes trouvées : {len(notes)}")

    for note in notes:
        seance_id_str = note.get("seanceId")
        if not seance_id_str:
            print(f"Note {_id} sans seanceId, skip")
            continue

        try:
            seance_id = ObjectId(seance_id_str)
        except Exception as e:
            print(f"Erreur conversion ObjectId pour seanceId {seance_id_str} : {e}")
            continue

        seance = seances_collection.find_one({"_id": seance_id})

        if not seance:
            print(f"Aucune séance trouvée pour seanceId: {seance_id_str}")
            continue

        joined_record = {
            "etudiantId": note.get("etudiantId"),
            "groupeId": note.get("groupeId"),
            "valeur": note.get("valeur"),
            "seanceId": seance_id_str,
            "seanceTitre": seance.get("titre"),
            "seanceDescription": seance.get("description"),
            "seanceDate": seance.get("date"),
            "typeNote": seance.get("typeNote")
        }

        joined_data.append(joined_record)

    if joined_data:
        # Vide la collection avant insert pour test (optionnel)
        output_collection.delete_many({})
        result = output_collection.insert_many(joined_data)
        print(f"Inséré {len(result.inserted_ids)} documents dans note_seance")
    else:
        print("Aucun document à insérer dans note_seance")

    # Calcul du classement des meilleurs étudiants par séance
    classement = []
    for seance_id_str in set([d["seanceId"] for d in joined_data]):
        notes_seance = [d for d in joined_data if d["seanceId"] == seance_id_str]
        notes_seance.sort(key=lambda x: x["valeur"], reverse=True)

        # Prépare top 3 étudiants pour la séance
        top_etudiants = notes_seance[:3]

        classement.append({
            "seanceId": seance_id_str,
            "seanceTitre": top_etudiants[0]["seanceTitre"] if top_etudiants else None,
            "topEtudiants": [{
                "etudiantId": e["etudiantId"],
                "valeur": e["valeur"],
                "badge": "🏆" if idx == 0 else "🥈" if idx == 1 else "🥉"
            } for idx, e in enumerate(top_etudiants)]
        })

    # Pour debug affichage classement
    print("Classement par séance :")
    for c in classement:
        print(c)

    # On peut aussi sauvegarder ce classement dans une autre collection si besoin
    # ex: note_db["classement"].delete_many({})
    # note_db["classement"].insert_many(classement)

    return {
        "note_seance_inserted": len(joined_data),
        "classement": classement
    }


if __name__ == "__main__":
    result = process_data()
    print("Process terminé.")
