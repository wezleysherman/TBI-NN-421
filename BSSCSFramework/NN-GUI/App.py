#using the tutorials here:
#(BASIC TUTORIAL) https://build-system.fman.io/pyqt5-tutorial INFO ON TURNING INTO STANDALONE .EXE AT THE END
#(FILE DIALOGS / BASIC GUI SETUP) http://zetcode.com/gui/pyqt5/dialogs/
#CURRENTLY NOT USED (FILE DIALOGS) https://pythonspot.com/pyqt5-file-dialog/
#CURRENTLY NOT USED (STYLES) http://doc.qt.io/qt-5/qtwidgets-index.html#styles

from PyQt5.QtWidgets import QApplication, QLabel, QWidget, QVBoxLayout, QFileDialog, QPushButton, QMessageBox, QLineEdit, QSlider
import sys

class NNGUI(QWidget):

    def __init__(self):
        super().__init__()
        self.initNNGUI()

    def initNNGUI(self):
        layout = QVBoxLayout()
		
        csv_filepath = QLineEdit(".CSV Filename Here")
        csv_filepath.setReadOnly(True)
        csv_filepick_button = QPushButton("Choose a .CSV File")
        def csv_filepick_clicked():
            csv_filepicker = QFileDialog.getOpenFileName(self, "CSV File Picker", "", "CSV (*.csv)")
            csv_filepath.setText(csv_filepicker[0])
        csv_filepick_button.clicked.connect(csv_filepick_clicked)
		
        csv_load_button = QPushButton("Load the Selected .CSV File")
        def csv_load_clicked():
            #implement here
            print("not yet implemented")
        csv_load_button.clicked.connect(csv_load_clicked)
        

        imgfolder_filepath = QLineEdit("Image Folder Filename Here")
        imgfolder_filepath.setReadOnly(True)
        imgfolder_dirpick_button = QPushButton("Choose an Image Folder")
        def imgfolder_dirpick_clicked():
            imgfolder_filepicker = QFileDialog.getExistingDirectory(self, "Image Folder File Picker", "")
            imgfolder_filepath.setText(imgfolder_filepicker)
        imgfolder_dirpick_button.clicked.connect(imgfolder_dirpick_clicked)

        imgfolder_load_button = QPushButton("Load all Images from Selected Folder")
        def imgfolder_load_clicked():
            #implement here
            print("not yet implemented")
        imgfolder_load_button.clicked.connect(imgfolder_load_clicked)

        train_button = QPushButton("Train Me!")
        def train_clicked():
            #implement here
            print("not yet implemented")
        train_button.clicked.connect(train_clicked)

        iter_label = QLabel("Iterations:")
        iter_text = QLineEdit()
        iter_text.setReadOnly(True)
        iter_slider = QSlider(0x1)
        iter_slider.setTickInterval(8)
        iter_slider.setTickPosition(2)
        
        batch_label = QLabel("Batch Size:")
        batch_text = QLineEdit()
        batch_text.setReadOnly(True)
        batch_slider = QSlider(0x1)
        batch_slider.setTickInterval(8)
        batch_slider.setTickPosition(2)
        
        layout.addWidget(csv_filepath)
        layout.addWidget(csv_filepick_button)
        layout.addWidget(csv_load_button)
        layout.addWidget(imgfolder_filepath)
        layout.addWidget(imgfolder_dirpick_button)
        layout.addWidget(imgfolder_load_button)
        layout.addWidget(iter_label)
        layout.addWidget(iter_slider)
        layout.addWidget(iter_text)
        layout.addWidget(batch_label)
        layout.addWidget(batch_slider)
        layout.addWidget(batch_text)
        layout.addWidget(train_button)
        
        self.setGeometry(300, 300, 350, 300)
        self.setLayout(layout)
        self.show()

if __name__ == "__main__":
    app = QApplication(sys.argv)
    app.setStyle("Fusion")
    nngui = NNGUI()
    sys.exit(app.exec_())
