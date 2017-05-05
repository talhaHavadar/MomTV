"""
    Handles all requests that coming from phone
"""
import socketserver
import bot
from bot import TVBot

class TCPSocketHandler(socketserver.StreamRequestHandler):
    """
        Handles the tcp socket connection
    """
    def handle(self):
        self.bot = TVBot()
        while True:
            self.data = self.rfile.readline()
            if not self.data:
                break
            self.data = self.data.decode()
            if "STAR" in self.data.upper():
                self.bot.open(bot.TV_STAR)
            elif "ATV" in self.data.upper():
                self.bot.open(bot.TV_ATV)
            elif "KANAL D" in self.data.upper() or "KANALD" in self.data.upper():
                self.bot.open(bot.TV_KANALD)
            elif "TRT" in self.data.upper():
                self.bot.open(bot.TV_TRT)
            elif "FOX" in self.data.upper():
                self.bot.open(bot.TV_FOX)
            elif "SHOW TV" in self.data.upper() or "SHOW" in self.data.upper():
                self.bot.open(bot.TV_SHOW)
            elif "TV2" in self.data.upper() or "TV 2" in self.data.upper():
                self.bot.open(bot.TV_TV2)
            elif "KAPAT" in self.data.upper() or "CLOSE" in self.data.upper():
                self.bot.close()
            
        self.bot.close()

