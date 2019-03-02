#using the tutorials here:
#(BASIC TUTORIAL) https://build-system.fman.io/pyqt5-tutorial INFO ON TURNING INTO STANDALONE .EXE AT THE END
#(FILE DIALOGS / BASIC GUI SETUP) http://zetcode.com/gui/pyqt5/dialogs/
#CURRENTLY NOT USED (FILE DIALOGS) https://pythonspot.com/pyqt5-file-dialog/
#CURRENTLY NOT USED (STYLES) http://doc.qt.io/qt-5/qtwidgets-index.html#styles
#(CSV FILES) https://realpython.com/python-csv/

from PyQt5.QtWidgets import QApplication, QLabel, QWidget, QVBoxLayout, QFileDialog, QPushButton, QMessageBox, QLineEdit, QSlider, QTextEdit, QGridLayout
from PyQt5.QtCore import QUrl, QFileInfo, Qt
import sys, pandas, os, webbrowser

class NNGUI(QWidget):

#creates GUI when started

    def __init__(self):
        super().__init__()
        self.initNNGUI()

#constructor for the main window
    def initNNGUI(self):

#layout of the window
        layout = QGridLayout()
        layout.setVerticalSpacing(5)
        layout.setHorizontalSpacing(10)
        self.setWindowTitle("Neural Network Customizer")

#.csv UI elements

        #.csv picker
        csv_filepath = QLineEdit(".CSV Filename Here")
        csv_filepath.setReadOnly(True)
        csv_path = QLabel()
        csv_filepick_button = QPushButton("Choose a .CSV File")
        def csv_filepick_clicked():
            csv_filepicker = QFileDialog.getOpenFileName(self, "CSV File Picker", "", "CSV (*.csv)")
            csv_filepath.setText(csv_filepicker[0].split("/")[len(csv_filepicker[0].split("/")) - 1])
            csv_path.setText(csv_filepicker[0])
            print("Selected: " + csv_filepath.text())
        csv_filepick_button.clicked.connect(csv_filepick_clicked)
        
        csv_text = QTextEdit("Your .CSV File Will Appear Here")
        csv_text.setReadOnly(True)

	#.csv loader
        csv_load_button = QPushButton("Load the Selected .CSV File")
        def csv_load_clicked():
            try:
                print("Loaded: " + csv_path.text())
                csv_file = pandas.read_csv(csv_path.text())
                print(csv_file)
                csv_text.setText(str(csv_file))
            except:
                print("There was an error loading the file.")
                
        csv_load_button.clicked.connect(csv_load_clicked)

        csv_name = QLabel("CSV File:")
        csv_spacer = QLabel()
        
#image folder UI elements

        #image folder picker
        imgfolder_filepath = QLineEdit("Image Folder Filename Here")
        imgfolder_filepath.setReadOnly(True)
        imgfolder_path = QLabel()
        imgfolder_dirpick_button = QPushButton("Choose an Image Folder")
        def imgfolder_dirpick_clicked():
            imgfolder_filepicker = QFileDialog.getExistingDirectory(self, "Image Folder File Picker", "")
            imgfolder_filepath.setText(imgfolder_filepicker.split("/")[len(imgfolder_filepicker.split("/")) - 1])
            imgfolder_path.setText(imgfolder_filepicker)
            print("Selected: " + imgfolder_filepath.text())
        imgfolder_dirpick_button.clicked.connect(imgfolder_dirpick_clicked)

        #image folder loader
        img_list = []
        imgfolder_load_button = QPushButton("Load all Images from Selected Folder")
        def imgfolder_load_clicked():
            try:
                print("Loaded: " + imgfolder_path.text())
                for img in os.listdir(imgfolder_path.text()):
                    img_list.append(img)
                for img in img_list:
                    print(img)
            except:
                print("There was an error loading the file.")
                
        imgfolder_load_button.clicked.connect(imgfolder_load_clicked)

        img_name = QLabel("Image Folder:")
        img_spacer = QLabel()

        custom_name = QLabel("Customizations:")
#iteration UI elements
        iter_label = QLabel("Iterations:")
        iter_label.setAlignment(Qt.AlignCenter)
        iter_text = QLineEdit()
        iter_text.setReadOnly(True)
        iter_slider = QSlider(0x1)
        iter_slider.setTickInterval(5)
        iter_slider.setTickPosition(2)
        iter_slider.setRange(0,50)

        def user_iters(value):
            iter_text.setText(str(value))
        iter_slider.valueChanged[int].connect(user_iters)

        iter_spacer = QLabel()

#batch UI elements
        batch_label = QLabel("Batch Size:")
        batch_label.setAlignment(Qt.AlignCenter)
        batch_text = QLineEdit()
        batch_text.setReadOnly(True)
        batch_slider = QSlider(0x1)
        batch_slider.setTickInterval(5)
        batch_slider.setTickPosition(2)
        batch_slider.setRange(0,50)

        def user_batchs(value):
            batch_text.setText(str(value))
        batch_slider.valueChanged[int].connect(user_batchs)

        batch_spacer = QLabel()

#layer UI elements

        layer_label = QLabel("Layer Amount:")
        layer_label.setAlignment(Qt.AlignCenter)
        layer_text = QLineEdit()
        layer_text.setReadOnly(True)
        layer_slider = QSlider(0x1)
        layer_slider.setTickInterval(5)
        layer_slider.setTickPosition(2)
        layer_slider.setRange(0,50)

        def user_layers(value):
            layer_text.setText(str(value))
        layer_slider.valueChanged[int].connect(user_layers)

        layer_spacer = QLabel()

#user nodes UI elements

        node_label = QLabel("Node Amount:")
        node_label.setAlignment(Qt.AlignCenter)
        node_text = QLineEdit()
        node_text.setReadOnly(True)
        node_slider = QSlider(0x1)
        node_slider.setTickInterval(5)
        node_slider.setTickPosition(2)
        node_slider.setRange(0,50)

        def user_nodes(value):
            node_text.setText(str(value))
        node_slider.valueChanged[int].connect(user_nodes)

        node_spacer = QLabel()
        
#tensorboard UI elements

        tb_filepath = QLineEdit("Tensorboard Log File Path Here")
        tb_filepath.setReadOnly(True)
        tb_path = QLabel()

        tb_filepick_button = QPushButton("Select Log Folder")
        def tb_filepick_clicked():
            tb_filepicker = QFileDialog.getExistingDirectory(self, "Log Folder File Picker", "")
            tb_filepath.setText(tb_filepicker.split("/")[len(tb_filepicker.split("/")) - 1])
            tb_path.setText(tb_filepicker)
            print("Selected: " + tb_filepath.text())
        tb_filepick_button.clicked.connect(tb_filepick_clicked)

        tb_button = QPushButton("Tensorboard")
        def tb_clicked():
            print("tb_button clicked")
            os.system("tensorboard --logdir=" + tb_path.text())
            print("Loaded: " + tb_path.text())
            webpath = "http://localhost:6006"
            webbrowser.open(webpath, new=2)
        tb_button.clicked.connect(tb_clicked)

        tb_name = QLabel("Tensorboard:")
        tb_spacer = QLabel()

#train UI elements
        train_button = QPushButton("Train Me!")
        def train_clicked():
            #implement here
            print("not yet implemented")
        train_button.clicked.connect(train_clicked)
        
#widgets added to layout
        layout.addWidget(csv_name, 1, 0, 1, 0)
        layout.addWidget(csv_filepath, 1, 1, 1, 3)
        layout.addWidget(csv_filepick_button, 2, 1, 1, 3)
        layout.addWidget(csv_load_button, 3, 1, 1, 3)
        layout.addWidget(csv_text, 4, 1, 1, 3)
        layout.addWidget(csv_spacer, 5, 1, 1, 3)
        
        layout.addWidget(img_name, 6, 0, 1, 0)
        layout.addWidget(imgfolder_filepath, 6, 1, 1, 3)
        layout.addWidget(imgfolder_dirpick_button, 7 ,1, 1, 3)
        layout.addWidget(imgfolder_load_button, 8, 1, 1 ,3)
        layout.addWidget(img_spacer, 9, 1, 1, 3)
        
        layout.addWidget(custom_name, 10, 0, 1, 0)
        layout.addWidget(iter_label, 10, 2, 1, 1)
        layout.addWidget(iter_slider, 11, 1, 1, 3)
        layout.addWidget(iter_text, 12, 1, 1, 3)
        layout.addWidget(iter_spacer, 13, 1, 1, 3)
        layout.addWidget(batch_label, 14, 2, 1, 1)
        layout.addWidget(batch_slider, 15, 1, 1, 3)
        layout.addWidget(batch_text, 16, 1, 1, 3)
        layout.addWidget(batch_spacer, 17, 1, 1, 3)
        layout.addWidget(layer_label, 18, 2, 1, 1)
        layout.addWidget(layer_slider, 19, 1, 1, 3)
        layout.addWidget(layer_text, 20, 1, 1, 3)
        layout.addWidget(layer_spacer, 21, 1, 1, 3)
        layout.addWidget(node_label, 22, 2, 1, 1)
        layout.addWidget(node_slider, 23, 1, 1, 3)
        layout.addWidget(node_text, 24, 1, 1, 3)
        layout.addWidget(node_spacer, 25, 1, 1, 3)
        
        layout.addWidget(tb_name, 26, 0, 1, 0)
        layout.addWidget(tb_filepath, 26, 1, 1, 3)
        layout.addWidget(tb_filepick_button, 27, 1, 1, 3)
        layout.addWidget(tb_button, 28, 1, 1, 3)
        layout.addWidget(tb_spacer, 29, 1, 1, 3)
        layout.addWidget(train_button, 30, 1, 1, 3)

#setup of window
        self.setGeometry(350, 350, 450, 350)
        self.setLayout(layout)
        self.show()
