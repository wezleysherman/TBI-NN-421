#using the tutorials here:
#(BASIC TUTORIAL) https://build-system.fman.io/pyqt5-tutorial INFO ON TURNING INTO STANDALONE .EXE AT THE END
#(FILE DIALOGS / BASIC GUI SETUP) http://zetcode.com/gui/pyqt5/dialogs/
#CURRENTLY NOT USED (FILE DIALOGS) https://pythonspot.com/pyqt5-file-dialog/
#CURRENTLY NOT USED (STYLES) http://doc.qt.io/qt-5/qtwidgets-index.html#styles

from PyQt5.QtWidgets import QApplication, QLabel, QWidget, QVBoxLayout, QFileDialog, QPushButton, QMessageBox, QLineEdit
import sys

class NNGUI(QWidget):

    def __init__(self):
        super().__init__()
        self.initNNGUI()

    def initNNGUI(self):
        layout = QVBoxLayout()
        csv_filepath = QLineEdit(".CSV Filename Here")
        csv_filepath.setReadOnly(True)
        csv_button = QPushButton("Choose a .CSV File")
        def csv_clicked():
            csv_filepicker = QFileDialog.getOpenFileName(self, "CSV File Picker", "", "CSV (*.csv)")
            csv_filepath.setText(csv_filepicker[0])
        csv_button.clicked.connect(csv_clicked)

        imgfolder_filepath = QLineEdit("Image Folder Filename Here")
        imgfolder_filepath.setReadOnly(True)
        imgfolder_button = QPushButton("Choose an Image Folder")
        def imgfolder_clicked():
            imgfolder_filepicker = QFileDialog.getExistingDirectory(self, "Image Folder File Picker", "")
            imgfolder_filepath.setText(imgfolder_filepicker)
        imgfolder_button.clicked.connect(imgfolder_clicked)
        
        layout.addWidget(csv_filepath)
        layout.addWidget(csv_button)
        layout.addWidget(imgfolder_filepath)
        layout.addWidget(imgfolder_button)
        self.setGeometry(300, 300, 350, 300)
        self.setLayout(layout)
        self.show()

if __name__ == "__main__":
    app = QApplication(sys.argv)
    app.setStyle("Fusion")
    nngui = NNGUI()
    sys.exit(app.exec_())
