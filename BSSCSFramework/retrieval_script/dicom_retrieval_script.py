import pyautogui as pyUI
import xlrd
import time

#Read excel file
file = 'C:\\Users\\bakerwyatt19\\Desktop\\CAPSTONE\\TBI-NN-421\\BSSCSFramework\\retrieval_script\\excelTests.xlsx'
pyUI.PAUSE = 1
pyUI.FAILSAFE = False

wb = xlrd.open_workbook(file)
sheet = wb.sheet_by_index(0)

dataArray = []
#Fill dataArray with dictionaries containing mrn and date data
for i in range(sheet.nrows):
    mrn = sheet.cell_value(i, 0)
    date = sheet.cell_value(i, 1)
    newEntry = {'mrn': mrn, 'date': date}
    dataArray.append(newEntry)

#Perform gui tasks for every mrn and date entry
print(pyUI.position())

click1 = (991, 264)
click2 = (795, 814)
click3 = (991, 264)
click4 = (1203, 264)
click5 = (1190, 300)
click6 = (1190, 300)
click7 = (1190, 300)
click8 = (1190, 300)
click9 = [1190, 300]
click10 = (580, 410)


#Tester just for seeing where the mouse is going for easier editting
try:
    for i in dataArray:
        print(i['mrn'])
        time.sleep(5)
        pyUI.typewrite(i['mrn'])
        pyUI.moveTo(click1, duration=.5)
        pyUI.moveTo(click2, duration=.5)
        pyUI.moveTo(click3, duration=.5)
        pyUI.moveTo(click4, duration=.5)
        pyUI.moveTo(click5, duration=.5)
        pyUI.moveTo(click6, duration=.5)
        pyUI.moveTo(click7, duration=.5)
        pyUI.moveTo(click8, duration=.5)
        pyUI.moveTo(click9, duration=.5)
        pyUI.moveTo(click10, duration=.5)

except KeyboardInterrupt:
    print('DONE')

#Actual script for walking through UI
'''
try:
    for i in dataArray:
        print(i['mrn'])
        time.sleep(5)
        pyUI.click(click1)    #Click MRN
        pyUI.typewrite(i['mrn'])#Type MRN
        pyUI.click(click2)    #Click Exam Date/Time
        pyUI.click(click3)    #Click start date
        pyUI.typewrite(i['date'])#Type Date
        pyUI.click(click4)   #Click modality
        pyUI.click(click5)   #Click CT
        pyUI.click(click6)   #Click Body Part
        pyUI.click(click7)   #Click Head, Brain
        pyUI.click(click8)   #Click search
        elementClicks = click9.copy()
        pyUI.keyDown('shift')
        for x in range(maxNumElements)  #Select all elements to be taken
            pyUI.click(elementClicks[0], elementClicks[1])
            elementClicks[1] += 10
        pyUI.keyUp('shift')
        pyUI.moveTo(click9) #Move back onto the first element to be taken
        pyUI.dragTo(click10, 1, button='left') #Move into the folder
        
except KeyboardInterrupt:
    print('DONE')
'''
