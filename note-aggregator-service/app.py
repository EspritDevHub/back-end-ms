from flask import Flask, jsonify
from processor import process_data

app = Flask(__name__)

@app.route("/process", methods=["GET"])
def process():
    result = process_data()
    return jsonify(result)

if __name__ == "__main__":
    app.run(port=5005)
