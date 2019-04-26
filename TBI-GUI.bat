@ECHO OFF
python -m pip install --upgrade pip
pip install nibabel
pip install matplotlib
cd ./TBI-GUI
gradle run
exit 0