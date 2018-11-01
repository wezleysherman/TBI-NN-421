''' Authors: Wezley Sherman
	Unit Tests for the data processing framework implementation
	Unit Test Reference: https://docs.python.org/2/library/unittest.html
'''
from DICOMImporter import DICOMImporter
from BSSCS_IMG_PROCESSING import BSSCS_IMG_PROCESSING
import unittest

class DataProcessingTests(unittest.TestCase):
	def test_dicom_importer(self):
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
		dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
		self.assertTrue(dicom_file, not None)
		self.assertFalse(dicom_pixel_arr.all(), None)
		return

	def test_contrast_adjust(self):
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
		dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
		contrast_applied_img = BSSCS_IMG_PROCESSING.contrast_image([dicom_pixel_arr], 2)
		contrast_applied_img_2 = BSSCS_IMG_PROCESSING.contrast_image([dicom_pixel_arr], 1)
		self.assertNotEqual(dicom_pixel_arr, contrast_applied_img[0])
		self.assertNotEqual(dicom_pixel_arr, contrast_applied_img_2[0])
		self.assertNotEqual(contrast_applied_img[0], contrast_applied_img_2[0])
		return

	def test_scale_adjust(self):
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')

		return

	def test_crop_adjust(self):
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')

		return

if __name__ == '__main__':
	unittest.main()
