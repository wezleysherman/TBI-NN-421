import pyautogui
import time
print("HELLO WORLD")
pyautogui.PAUSE = 1;
pyautogui.FAILSAFE = True
print(pyautogui.position())
pyautogui.moveTo(331, 293, duration=.5)
pyautogui.moveTo(795, 238, duration=.5)
pyautogui.moveTo(795, 814, duration=.5)
pyautogui.moveTo(991, 264, duration=.5)
pyautogui.moveTo(1203, 264, duration=.5)
pyautogui.moveTo(1190, 300, duration=.5)




'''width, height = pyautogui.size();
try:
    pyautogui.moveTo(540, 14, duration=5)
    pyautogui.click()
except KeyboardInterrupt:
    print('DONE')
'''
###pyautogui.click()
