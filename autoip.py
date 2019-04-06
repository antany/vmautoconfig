#!/usr/bin/python
import sys

def process(str):
    data = str.split(" ");
    if data[0]==sys.argv[1]:
        f = open('/etc/hostname','w',encoding='utf-8')
        f.write(sys.argv[1]);
        f.close();
        f = open('/etc/dhcpcd.conf','a+',encoding='utf-8')
        f.write("interface enp0s3\n");
        f.write("static ip_address="+data[1]+"/24\n");
        f.write("static routers=10.0.2.1\n")
        f.write("static domain_name_server=192.168.0.1\n")
        f.write("\n\ninterface enp0s8\n");
        f.write("static ip_address="+data[2]+"/24\n");

def update_host(str):
    data = str.split(" ");
    if len(data) > 2 :
        f = open('/etc/hosts','a+',encoding='utf-8')
        f.write(data[2]+" "+data[0]+"\n");
        f.close();

dataString = """base 10.0.2.15 192.168.56.15
serv1 10.0.2.151 192.168.56.151
serv2 10.0.2.152 192.168.56.152
serv3 10.0.2.153 192.168.56.153
serv4 10.0.2.154 192.168.56.154"""



lines = dataString.split("\n");

for line in lines:
    process(line);
    update_host(line);
  
  		
