import random
import requests

java_api_url = "https://ei-pedro-sante.appspot.com/"
i = 1
while i < 99:
    try:

        c = round((random.random() * 150 / i) + 85, 3)
        d = round((random.random() * 100 / i), 3)
        data = str(c) + "," + str(d)
        # a = random.randint(1,10)
        # b = random.randint(1,10)
        # data = str(a) + "," + str(b)
        response = requests.post(java_api_url + "sql", data=data)
        print("data: {}".format(data))
        i = i + 1
    except Exception:
        pass
