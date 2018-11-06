import pyautogui
import time
print("HELLO WORLD")
pyautogui.PAUSE = 1;
pyautogui.FAILSAFE = True
width, height = pyautogui.size();
try:
    pyautogui.moveTo(540, 14, duration=5)
    pyautogui.click()
except KeyboardInterrupt:
    print('DONE')
###pyautogui.click()
