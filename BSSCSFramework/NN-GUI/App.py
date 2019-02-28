#using the tutorials here:
#(BASIC TUTORIAL) https://build-system.fman.io/pyqt5-tutorial INFO ON TURNING INTO STANDALONE .EXE AT THE END
#(FILE DIALOGS / BASIC GUI SETUP) http://zetcode.com/gui/pyqt5/dialogs/
#CURRENTLY NOT USED (FILE DIALOGS) https://pythonspot.com/pyqt5-file-dialog/
#CURRENTLY NOT USED (STYLES) http://doc.qt.io/qt-5/qtwidgets-index.html#styles
#(CSV FILES) https://realpython.com/python-csv/

from PyQt5.QtWidgets import QApplication
import sys, NNGUI

#Main Method
if __name__ == "__main__":
    app = QApplication(sys.argv)
    app.setStyle("Fusion")
    nngui = NNGUI.NNGUI()
    sys.exit(app.exec_())
