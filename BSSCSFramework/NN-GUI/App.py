#using the tutorial here: https://build-system.fman.io/pyqt5-tutorial INFO ON TURNING INTO STANDALONE .EXE AT THE END
#CURRENTLY NOT USED (FILE DIALOGS) https://pythonspot.com/pyqt5-file-dialog/
#CURRENTLY NOT USED (STYLES) http://doc.qt.io/qt-5/qtwidgets-index.html#styles

from PyQt5.QtWidgets import QApplication, QLabel, QWidget, QVBoxLayout, QFileDialog, QPushButton, QMessageBox
app = QApplication([])
app.setStyle("Fusion")
window = QWidget()
layout = QVBoxLayout()

csv_button = QPushButton("Choose a .CSV File")
def csv_clicked():
    csv_filepicker = QFileDialog()
    """alert = QMessageBox()
    alert.setText("Insert .CSV File Picker Here!")
    alert.exec_()"""
csv_button.clicked.connect(csv_clicked)

layout.addWidget(csv_button)
window.setLayout(layout)
window.show()
app.exec_()

