''' Authors: Wezley Sherman
	Unit Tests for the data processing framework implementation
	Unit Test Reference: https://docs.python.org/2/library/unittest.html
'''
from DICOMImporter import DICOMImporter
from BSSCS_IMG_PROCESSING import BSSCS_IMG_PROCESSING
from UNET_Data import UNET_DATA
import numpy as np
import unittest

class DataProcessingTests(unittest.TestCase):
	def test_dicom_importer(self):
		''' Handles testing the dicom importer to ensure it's able to import the test file

			Preconditions: No test file has been loaded
			Postconditions: Test file has successfully been loaded
		'''
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
		dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
		self.assertTrue(dicom_file, not None)
		self.assertFalse(dicom_pixel_arr.all(), None)

	def test_contrast_adjust(self):
		''' Handles testing of the contrast adjust feature in the image processing tool set

			Preconditions: Dicom image without any contrast adjustment
			Postconditions: Dicom image with adjusted contrast has been tested
		'''
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
		dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
		contrast_applied_img = BSSCS_IMG_PROCESSING.contrast_image([dicom_pixel_arr], 2)
		contrast_applied_img_2 = BSSCS_IMG_PROCESSING.contrast_image([dicom_pixel_arr], 1)

		self.assertNotEqual(dicom_pixel_arr, contrast_applied_img[0])
		self.assertNotEqual(dicom_pixel_arr, contrast_applied_img_2[0])
		self.assertNotEqual(contrast_applied_img[0], contrast_applied_img_2[0])

	def test_scale_adjust(self):
		''' Handles testing the scale adjustment of the images

			Preconditions: Dicom image without any scale adjustments
			Postconditions: Scaled dicom Image that has been tested
		'''
		scale_factor = 0.1
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
		dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
		o_width, o_height = dicom_pixel_arr.shape
		scaled_dicom = BSSCS_IMG_PROCESSING.scale_image([dicom_pixel_arr], scale_factor)
		n_width, n_height = scaled_dicom[0].size

		self.assertNotEqual(o_width, n_width)
		self.assertNotEqual(o_height, n_height)
		self.assertTrue((o_width-2) == (n_width / scale_factor))
		self.assertTrue((o_height-2) == (n_height / scale_factor))

		scaled_dicom = BSSCS_IMG_PROCESSING.scale_image([dicom_pixel_arr], 10)
		n_width, n_height = scaled_dicom[0].size

		self.assertTrue(o_width < n_width)
		self.assertTrue(o_height < n_height)

	def test_crop_adjust(self):
		''' Handles testing the crop adjustment of the images

			Preconditions: Dicom image without any crop adiustments
			Postconditions: Croped dicom image that has been tested
		'''
		crop_pixels = 100
		dicom_file = DICOMImporter.open_dicom_file('test_dicom/test_dicom.dcm')
		dicom_pixel_arr = DICOMImporter.get_dicom_pixel_array(dicom_file)
		o_width, o_height = dicom_pixel_arr.shape
		cropped_dicom = BSSCS_IMG_PROCESSING.crop_image([dicom_pixel_arr], crop_pixels)
		n_width, n_height = cropped_dicom[0].size

		self.assertTrue(o_width == (n_width + 200))
		self.assertTrue(o_height == (n_height + 200))
		self.assertNotEqual(n_width, o_width)
		self.assertNotEqual(n_height, o_height)

	def test_unet_data(self):
		''' Handles testing the unet dataclass
	
			Preconditions: No data has been imported
			Postconditions: Data has been correctly imported with assigned labels
		'''
		unet = UNET_DATA()
		
		# various test cases to ensure test data has properly been loaded
		self.assertNotEqual(unet.import_labels_from_csv("test_csv.csv")[1], "has_tbi")
		self.assertFalse(unet.fetch_data("test_csv.csv"), None)
		
if __name__ == '__main__':
	unittest.main()
