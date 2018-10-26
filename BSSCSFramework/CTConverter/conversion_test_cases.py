import os

from DICOM_operations import DICOM_2_NIFTI, decompress_DICOM

#decompress images. delete test_images/knee/dicom_raw unless you want to test exception handling
decompress_DICOM.decompress_series(os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/dicom")
#convert images. throws a weird exception for now but it works. use a nifti viewer to view
#(http://ric.uthscsa.edu/mango/papaya/index.html is a browser one)
DICOM_2_NIFTI.convert_dicom_series(os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/dicom_raw")
