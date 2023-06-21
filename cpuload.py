from multiprocessing import  Process
import time
import psutil
import logging

log = logging.getLogger("python_agent")

processes = []

def cpu_load(load,duration):
    load = int(load)
    duration = round(float(duration))
    start_time = round(time.time() * 1000)
    current_time = round(time.time() * 1000)
    while (current_time - start_time) < duration :
        if current_time % 100 == 0:
            time.sleep((100 - load)/1000)
        current_time = round(time.time() * 1000)

log.error("Current cpu usage: {}".format(str(psutil.cpu_percent(interval=1))))
cpu_count = psutil.cpu_count(logical=True)
print("Cpu Count : ",cpu_count)
for i in range(1, cpu_count + 1):
        p1 = Process(target=cpu_load, args=(100,60000))
        processes.append(p1)
        p1.start()