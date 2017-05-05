"""
    Main script of the program
"""
import server
import socketserver


if __name__ == '__main__':
    HOST, PORT = "", 4444
    s = socketserver.TCPServer((HOST, PORT), server.TCPSocketHandler)
    s.serve_forever()

# import time
# import bot
# from bot import TVBot
# import socket

# with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as s:
#     s.bind(('', 4444))
#     s.listen(1)
#     conn, addr = s.accept()

#     print("Connection established: ", addr)
#     while True:
#         data = conn.recv(1024)
#         if not data: break
#         print("data:", data.decode())

# BOT = TVBot()
# BOT.open(bot.TV_ATV)
# time.sleep(2)
# BOT.open(bot.TV_FOX)
# time.sleep(2)
# BOT.open(bot.TV_KANALD)
# time.sleep(2)
# BOT.open(bot.TV_SHOW)
# time.sleep(2)
# BOT.open(bot.TV_STAR)
# time.sleep(2)
# BOT.open(bot.TV_TRT)
# time.sleep(2)
# BOT.open(bot.TV_TV2)
# time.sleep(3)
# BOT.close()
