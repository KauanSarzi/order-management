import json
import os
import urllib.request
import urllib.error
from datetime import datetime, timezone


def lambda_handler(event, context):
    api_base_url = os.environ.get("API_BASE_URL", "http://localhost:8080")
    url = f"{api_base_url}/api/dashboard/stats"

    try:
        with urllib.request.urlopen(url, timeout=10) as response:
            dados = json.loads(response.read().decode())
    except urllib.error.URLError as e:
        return {
            "statusCode": 502,
            "headers": {
                "Content-Type": "application/json",
                "Access-Control-Allow-Origin": "*",
            },
            "body": json.dumps({"erro": f"Falha ao contatar a API: {str(e.reason)}"}),
        }

    relatorio = {
        "fonte": "AWS Lambda",
        "funcao": context.function_name if context else "oms-report",
        "versaoRelatorio": "1.0",
        "geradoEm": datetime.now(timezone.utc).isoformat(),
        "dados": dados,
    }

    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
        },
        "body": json.dumps(relatorio),
    }
