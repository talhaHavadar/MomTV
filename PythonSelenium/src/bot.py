"""
    Implementation of bot that automates browsing
    for watch live broadcasts of tvs.
"""
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.action_chains import ActionChains

TV_NONE = None
TV_FOX = 1
TV_STAR = 2
TV_KANALD = 3
TV_SHOW = 4
TV_TRT = 5
TV_ATV = 6
TV_TV2 = 7


class TVBot(object):
    """
        Handles automated operations
    """

    def __init__(self):
        self.current_tv = TV_NONE
        self.driver = webdriver.Chrome()
        self.driver.maximize_window()
        self.closed = False

    def open(self, tv_id):
        """
            Opens specified tv channel to watch.
            Available TV channels are listed below:
                TV_FOX = 1
                TV_STAR = 2
                TV_KANALD = 3
                TV_SHOW = 4
                TV_TRT = 5
                TV_ATV = 6
                TV_TV2 = 7
        """
        if self.closed:
            self.driver = webdriver.Chrome()
            self.closed = False
            self.driver.maximize_window()
        try:
            if tv_id == TV_FOX:
                self.driver.get("http://www.fox.com.tr/canli-yayin")
                self.current_tv = TV_FOX
                self.driver.find_element_by_css_selector("button..vjs-fullscreen-control.vjs-control.vjs-button").click()
            elif tv_id == TV_STAR:
                self.driver.get("https://www.youtube.com/watch?v=jWP3ntl64I4")
                self.current_tv = TV_STAR
                self.driver.find_element_by_css_selector("button.ytp-fullscreen-button.ytp-button").click()
            elif tv_id == TV_KANALD:
                self.driver.get("https://www.kanald.com.tr/canli-yayin")
                self.current_tv = TV_KANALD
                self.driver.find_element_by_css_selector("button.vjs-fullscreen-control.vjs-control.vjs-button").click()
            elif tv_id == TV_SHOW:
                self.driver.get("http://www.showtv.com.tr/canli-yayin")
                self.current_tv = TV_SHOW
                self.driver.find_element_by_css_selector("button.vjs-fullscreen-control.vjs-control.vjs-button").click()
            elif tv_id == TV_TRT:
                self.driver.get("http://www.trt.net.tr/anasayfa/canli.aspx?y=tv&k=trt1")
                self.current_tv = TV_TRT
                self.driver.find_element_by_css_selector('#trtnettrjwplayer').click()
                self.driver.find_element_by_css_selector(".jw-icon.jw-icon-inline.jw-button-color.jw-reset.jw-icon-fullscreen").click()            
            elif tv_id == TV_ATV:
                self.driver.get("http://www.atv.com.tr/webtv/canli-yayin")
                self.current_tv = TV_ATV
                self.driver.find_element_by_css_selector('div.player').click()
                self.driver.find_element_by_css_selector(".jw-icon.jw-icon-inline.jw-button-color.jw-reset.jw-icon-fullscreen").click()
                self.driver.find_element_by_css_selector('div.player').click()
            elif tv_id == TV_TV2:
                self.driver.get("http://www.teve2.com.tr/canli-yayin")
                self.current_tv = TV_TV2
                self.driver.find_element_by_css_selector('#player-container').click()
                self.driver.find_element_by_css_selector('button.vjs-fullscreen-control.vjs-control.vjs-button').click()
        except:
            pass

    def close(self):
        if not self.closed:
            self.driver.close()
            self.closed = True
