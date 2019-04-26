@ECHO OFF
python -m pip install --upgrade pip
pip install PyQt5
pip install pandas
pip install tensorflow
pip install PLL
pip install Pillow
cd ./BSSCSFramework/NN-GUI
python App.py
exit 0