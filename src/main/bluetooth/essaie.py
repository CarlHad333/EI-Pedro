import random
import requests

java_api_url = "https://ei-pedro-sante.appspot.com/"
data = "5,6,7"
try:
    response = requests.post(java_api_url + "sql", data=data)
    print("data: {}".format(data))
except Exception:
    pass