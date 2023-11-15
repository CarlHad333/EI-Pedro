import asyncio
from bleak import BleakScanner
from bleak import BleakClient
async def main():
    target_name = "ESP32-PEDRO-DISPLAY"
    target_address = None
    SERVICE_UUID=             "DFCD0001-36E1-4688-B7F5-EA07361B26A8"
    CHARACTERISTIC_UUID=     "DFCD000A-36E1-4688-B7F5-EA07361B26A8"
    devices = await BleakScanner.discover()
    for d in devices:
        print(d)
        if target_name == d.name:
            target_address = d.address
            print("found target {} bluetooth device with address {} ".format(target_name,target_address))
            break
    if target_address is not None:        
        async with BleakClient(target_address) as client:
            print(f"Connected: {client.is_connected}")
                
            while 1:
                try:
                    data = await client.read_gatt_char(CHARACTERISTIC_UUID)
                    data = data.decode('utf-8') #convert byte to str
                    print("data: {}".format(data))
                except Exception:
                    pass
                
            
    else:
        print("could not find target bluetooth device nearby")
asyncio.run(main())
