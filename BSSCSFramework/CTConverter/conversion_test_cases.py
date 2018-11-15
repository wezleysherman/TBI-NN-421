import os
import unittest
import shutil
import dicom2nifti
import numpy
from retrying import retry
from DICOM_operations import DICOM_2_NIFTI, decompress_DICOM

dicom_input_path = os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/dicom"
dicom_output_path = os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/dicom_raw"
nifti_output_folder = os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/nifti"
nifti_output_path = os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/nifti/knee.nii"

#decompress images. delete test_images/knee/dicom_raw unless you want to test exception handling
#decompress_DICOM.decompress_series(os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/dicom")
#convert images. throws a weird exception for now but it works. use a nifti viewer to view
#(http://ric.uthscsa.edu/mango/papaya/index.html is a browser one)
#DICOM_2_NIFTI.convert_dicom_series(os.path.dirname(os.path.abspath(__file__)) + "/test_images/knee/dicom_raw")

class TestDecompression(unittest.TestCase):

    @retry(stop_max_attempt_number=5, wait_fixed=2000)
    def setUp(self):
        if os.path.exists(dicom_output_path):
            shutil.rmtree(dicom_output_path)

    def test_successful_decompression(self):
        decompress_DICOM.decompress_series(dicom_input_path)
        self.assertTrue(os.path.exists(dicom_output_path))

    def test_repeat_decompression(self):
        decompress_DICOM.decompress_series(dicom_input_path)
        with self.assertRaisesRegex(FileExistsError, "This file has already been decompressed. Raw files available at") as ex:
            decompress_DICOM.decompress_series(dicom_input_path)

class TestConversion(unittest.TestCase):

    @retry(stop_max_attempt_number=5, wait_fixed=2000)
    def setUp(self):
        if os.path.exists(dicom_output_path):
            shutil.rmtree(dicom_output_path)
        if os.path.exists(nifti_output_folder):
            shutil.rmtree(nifti_output_folder)

    def test_successful_conversion(self):
        decompress_DICOM.decompress_series(dicom_input_path)
        nifti_array = DICOM_2_NIFTI.convert_dicom_series(dicom_output_path)
        self.assertEqual(numpy.core.memmap, type(nifti_array))
        self.assertTrue(os.path.exists(nifti_output_folder))

    def test_compressed_data(self):
        with self.assertRaisesRegex(dicom2nifti.exceptions.ConversionError, "Cannot convert compressed files.") as ex:
            DICOM_2_NIFTI.convert_dicom_series(dicom_input_path)

if __name__ == '__main__':
    unittest.main()
