import socket
import sys
import os
import random
from socket_wrapper import SocketWrapper

def connect():
    server_address = ('localhost', 8989)
    s = socket.socket()
    s.connect(server_address)
    socket_wrapper = SocketWrapper(s)
    print('Connection to server made.')
    print(socket_wrapper.read_line())
    line = ""
    while (True):
        hexagons = socket_wrapper.read_line()
        print("Received map: " + hexagons)
        socket_wrapper.send("Received map, here are the instructions! \n")
    s.close()

if __name__ == "__main__":
    connect()