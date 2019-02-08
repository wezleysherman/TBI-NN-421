#using the tutorials here:
#(BASIC TUTORIAL) https://build-system.fman.io/pyqt5-tutorial INFO ON TURNING INTO STANDALONE .EXE AT THE END
#(FILE DIALOGS / BASIC GUI SETUP) http://zetcode.com/gui/pyqt5/dialogs/
#CURRENTLY NOT USED (FILE DIALOGS) https://pythonspot.com/pyqt5-file-dialog/
#CURRENTLY NOT USED (STYLES) http://doc.qt.io/qt-5/qtwidgets-index.html#styles
#(CSV FILES) https://realpython.com/python-csv/

from PyQt5.QtWidgets import QApplication, QLabel, QWidget, QVBoxLayout, QFileDialog, QPushButton, QMessageBox, QLineEdit, QSlider, QTextEdit
import sys, pandas, os

class NNGUI(QWidget):

#creates GUI when started

    def __init__(self):
        super().__init__()
        self.initNNGUI()

#constructor for the main window
    def initNNGUI(self):

#layout of the window
        layout = QVBoxLayout()

#.csv UI elements
        csv_filepath = QLineEdit(".CSV Filename Here")
        csv_filepath.setReadOnly(True)
        csv_filepick_button = QPushButton("Choose a .CSV File")
        def csv_filepick_clicked():
            csv_filepicker = QFileDialog.getOpenFileName(self, "CSV File Picker", "", "CSV (*.csv)")
            csv_filepath.setText(csv_filepicker[0])
            print("Selected: " + csv_filepath.text())
        csv_filepick_button.clicked.connect(csv_filepick_clicked)
        
        csv_text = QTextEdit("Your .CSV File Will Appear Here")
        csv_text.setReadOnly(True)
	    
        csv_load_button = QPushButton("Load the Selected .CSV File")
        def csv_load_clicked():
            print("Loaded: " + csv_filepath.text())
            csv_file = pandas.read_csv(csv_filepath.text())
            print(csv_file)
            csv_text.setText(str(csv_file))
        csv_load_button.clicked.connect(csv_load_clicked)

        csv_spacer = QLabel()
        
#image folder UI elements
        imgfolder_filepath = QLineEdit("Image Folder Filename Here")
        imgfolder_filepath.setReadOnly(True)
        imgfolder_dirpick_button = QPushButton("Choose an Image Folder")
        def imgfolder_dirpick_clicked():
            imgfolder_filepicker = QFileDialog.getExistingDirectory(self, "Image Folder File Picker", "")
            imgfolder_filepath.setText(imgfolder_filepicker)
            print("Selected: " + imgfolder_filepath.text())
        imgfolder_dirpick_button.clicked.connect(imgfolder_dirpick_clicked)

        img_list = []
        imgfolder_load_button = QPushButton("Load all Images from Selected Folder")
        def imgfolder_load_clicked():
            print("Loaded: " + imgfolder_filepath.text())
            for img in os.listdir(imgfolder_filepath.text()):
                img_list.append(img)
            for img in img_list:
                print(img)
                
        imgfolder_load_button.clicked.connect(imgfolder_load_clicked)

        img_spacer = QLabel()

#train UI elements
        train_button = QPushButton("Train Me!")
        def train_clicked():
            #implement here
            print("not yet implemented")
        train_button.clicked.connect(train_clicked)

#iteration UI elements
        iter_label = QLabel("Iterations:")
        iter_text = QLineEdit()
        iter_text.setReadOnly(True)
        iter_slider = QSlider(0x1)
        iter_slider.setTickInterval(4)
        iter_slider.setTickPosition(2)
        iter_slider.setRange(0,32)

        def user_iters(value):
            iter_text.setText(str(value))
        iter_slider.valueChanged[int].connect(user_iters)

        iter_spacer = QLabel()

#batch UI elements
        batch_label = QLabel("Batch Size:")
        batch_text = QLineEdit()
        batch_text.setReadOnly(True)
        batch_slider = QSlider(0x1)
        batch_slider.setTickInterval(4)
        batch_slider.setTickPosition(2)
        batch_slider.setRange(0,32)

        def user_batchs(value):
            batch_text.setText(str(value))
        batch_slider.valueChanged[int].connect(user_batchs)

        batch_spacer = QLabel()

#widgets added to layout
        layout.addWidget(csv_filepath)
        layout.addWidget(csv_filepick_button)
        layout.addWidget(csv_load_button)
        layout.addWidget(csv_text)
        layout.addWidget(csv_spacer)
        layout.addWidget(imgfolder_filepath)
        layout.addWidget(imgfolder_dirpick_button)
        layout.addWidget(imgfolder_load_button)
        layout.addWidget(img_spacer)
        layout.addWidget(iter_label)
        layout.addWidget(iter_slider)
        layout.addWidget(iter_text)
        layout.addWidget(iter_spacer)
        layout.addWidget(batch_label)
        layout.addWidget(batch_slider)
        layout.addWidget(batch_text)
        layout.addWidget(batch_spacer)
        layout.addWidget(train_button)

#setup of window
        self.setGeometry(350, 350, 400, 350)
        self.setLayout(layout)
        self.show()

#Main Method
if __name__ == "__main__":
    app = QApplication(sys.argv)
    app.setStyle("Fusion")
    nngui = NNGUI()
    sys.exit(app.exec_())
