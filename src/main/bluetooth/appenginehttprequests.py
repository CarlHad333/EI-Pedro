import requests

java_api_url = "https://ei-pedro-sante.appspot.com/"
data_to_send = "One"

response = requests.post(java_api_url + "lol", json=data_to_send)

if response.status_code == 200:
    print("Data sent successfully")
    print(response.text)
else:
    print(f"Failed to send data. Status code: {response.status_code}")

