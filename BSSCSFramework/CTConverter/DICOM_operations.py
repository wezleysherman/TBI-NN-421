import dicom2nifti #documentation at https://media.readthedocs.org/pdf/dicom2nifti/latest/dicom2nifti.pdf
import nibabel #documentation at http://nipy.org/nibabel
from retrying import retry
import os
import sys
import glob
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from DICOMImporter import DICOMImporter

class prepare_scans:
    def decompress_and_convert(compressed_path):
        raw_path = prepare_scans.decompress(compressed_path)
        nifti_path = prepare_scans.convert(raw_path)
        return nifti_path

    @retry(stop_max_attempt_number=5, wait_fixed=2000)
    def convert(input_path):
        try:
            return DICOM_2_NIFTI.convert_dicom_series(input_path)
        except ConversionError:
            debug("nope")

    @retry(stop_max_attempt_number=5, wait_fixed=2000)
    def decompress(input_path):
        try:
            return decompress_DICOM.decompress_series(input_path)
        except FileExistsError:
            debug("Already decompressed this!")


class DICOM_2_NIFTI:
    def convert_dicom_series(input_path):
        ''' Converts a raw DICOM file series into a Nifti file.
            Saves a Nifti file to the file system based on path of DICOM file.

			Input:
				input_path:
                    - a directory path to a valid series of DICOM images (more than 1)
                    - sample directory layout /patient/date/dicom_raw/*.dcm will result in
                        nifti file located at /patient/date/nifti/date.nii
			Returns:
				Path of newly created Nifti file
            Raises:
                ConversionError if a) files are not decompressed or b) conversion fails
		'''
        folder = os.path.dirname(input_path)
        output_path = folder + "/nifti/"
        if not os.path.exists(output_path):
            os.mkdir(output_path)

        output_path += os.path.basename(os.path.dirname(input_path)) + ".nii"
        open(output_path, 'a').close()
        dicom_array = DICOMImporter.open_dicom_from_folder(input_path)

        try:
            dicom2nifti.convert_dicom.dicom_array_to_nifti(dicom_array, output_path)
        except NotImplementedError:
            debug("You tried to convert compressed files.")
            raise dicom2nifti.exceptions.ConversionError("Cannot convert compressed files.")
        except OSError:
            debug("OSError in conversion. Not something to worry about.")
            open(output_path, 'a').close()
        except dicom2nifti.exceptions.ConversionError:
            debug("Conversion Failed")
            return null

        return output_path

    def get_nifti_array(nifti_path):
        ''' Converts a Nifti file into a Numpy array that represents the 3D image

			Input:
				input_path:
                    - a directory path to a valid nifti file
			Returns:
				Numpy array representative of Nifti file
		'''
        nifti_img = nibabel.load(nifti_path)
        nifti_data = nifti_img.get_fdata()
        open(nifti_path, 'a').close()
        return nifti_data


class decompress_DICOM:
    def decompress_series(input_path):
        ''' Decompresses compressed DICOM files and stores them based on path of compressed files

			Input:
				input_path:
                    - a directory path to a valid series of DICOM images (more than 1)
                    - sample directory layout /patient/date/dicom/*.dcm will result in
                        raw files located at /patient/date/dicom_raw/*.dcm
            Returns:
                file path of folder holding decompressed DICOMs
            Raises:
                FileExistsError if the requested filepath has already been converted
		'''
        raw_path = os.path.dirname(input_path) + "/dicom_raw"
        if not os.path.exists(raw_path):
            os.mkdir(raw_path)
            for dicom_path in glob.glob(input_path + '/*.dcm'):
                os.system("gdcmconv --raw " + dicom_path + " " + os.path.dirname(os.path.dirname(dicom_path)) \
                            + "/dicom_raw/raw_" + os.path.basename(dicom_path))
        else:
            raise FileExistsError("This file has already been decompressed. Raw files available at " + raw_path)
        return raw_path

    def decompress_single(dicom_path):
        ''' Decompresses a single DICOM file and stores it based on path of compressed file

			Input:
				input_path:
                    - a directory path to a valid DICOM image (more than 1)
                    - sample directory layout /patient/date/dicom/*.dcm will result in
                        raw file located at /patient/date/dicom_raw/*.dcm
		'''
        os.system("gdcmconv --raw " + dicom_path + " " + os.path.dirname(dicom_path) \
                    + "/raw_" + os.path.basename(dicom_path))

def debug(output):
    print(output)
