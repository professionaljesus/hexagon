class SocketWrapper:
    def __init__(self, socket):
        self.socket = socket

    def send(self, message):
        data = bytes(message, 'utf-8')
        totalsent = 0
        while totalsent < len(data):
            # try to send as much data as possible in one go
            # send returns number of bytes successfully sent
            sent = self.socket.send(data[totalsent:])
            if sent == 0:
                raise RuntimeError("socket connection broken")
            totalsent = totalsent + sent

    def read_line(self):
        bytes = []

        # Read one byte at a time until we've read a new line character
        while True:
            byte = self.socket.recv(1)
            if byte == b'':
                raise RuntimeError("socket connection broken")
            elif byte == b'\n':
                break
            else:
                bytes.append(byte)

        # Remove carrige return if there is one
        if bytes[-1] == b'\r':
            bytes = bytes[:-1]

        # Convert to utf-8 string instead of bytestring
        return str(b''.join(bytes), 'utf-8')

    def close(self):
        self.socket.close()