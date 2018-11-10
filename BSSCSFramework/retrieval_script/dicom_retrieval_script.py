import pyautogui as pyUI
import xlrd
import time

#Read excel file
file = 'C:\\Users\\Freyr\\Desktop\\CAPSTONE\\excelTests.xlsx'

wb = xlrd.open_workbook(file)
sheet = wb.sheet_by_index(0)

dataArray = []
#Fill dataArray with dictionaries containing mrn and date data
for i in range(sheet.nrows):
    mrn = sheet.cell_value(i, 0)
    date = sheet.cell_value(i, 1)
    newEntry = {'mrn': mrn, 'date': date}
    pyUI.PAUSE = 1
    dataArray.append(newEntry)

#Perform gui tasks for every mrn and date entry
pyUI.FAILSAFE = True
print(pyUI.position())
try:
    for i in dataArray:
        print(i['mrn'])
        time.sleep(5)
        pyUI.typewrite(i['mrn'])
        pyUI.moveTo(331, 293, duration=.5)
        pyUI.moveTo(795, 238, duration=.5)
        pyUI.moveTo(795, 814, duration=.5)
        pyUI.moveTo(991, 264, duration=.5)
        pyUI.moveTo(1203, 264, duration=.5)
        pyUI.moveTo(1190, 300, duration=.5)
except KeyboardInterrupt:
    print('DONE')
###pyautogui.click()
