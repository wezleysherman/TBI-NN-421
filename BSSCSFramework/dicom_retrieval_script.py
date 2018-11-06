import pyautogui
import time
print("HELLO WORLD")
pyautogui.PAUSE = 1;
pyautogui.FAILSAFE = True
width, height = pyautogui.size();

pyautogui.moveTo(540, 14, duration=.25)
pyautogui.click()
###pyautogui.click()
