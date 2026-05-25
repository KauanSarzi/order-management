import json
import os
import urllib.request
import urllib.error


def lambda_handler(event, context):
    api_base_url = os.environ.get("API_BASE_URL", "http://localhost:8080")
    url = f"{api_base_url}/api/dashboard/stats"

    try:
        with urllib.request.urlopen(url, timeout=10) as response:
            data = json.loads(response.read().decode())
    except urllib.error.URLError as e:
        return {
            "statusCode": 502,
            "headers": {"Content-Type": "application/json"},
            "body": json.dumps({"error": f"Failed to reach API: {str(e.reason)}"}),
        }

    return {
        "statusCode": 200,
        "headers": {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
        },
        "body": json.dumps(data),
    }
