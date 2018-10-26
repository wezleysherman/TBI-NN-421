import dicom2nifti #documentation at https://media.readthedocs.org/pdf/dicom2nifti/latest/dicom2nifti.pdf
import os
import sys
import glob
sys.path.append(os.path.dirname(os.path.dirname(os.path.abspath(__file__))))
from BSSCSFramework.DICOMImporter import DICOMImporter

class DICOM_2_NIFTI:
    #converts a full series of dicom images into a nifti image
    def convert_dicom_series(input_path):
        folder = os.path.dirname(input_path)
        output_path = folder + "/nifti/"
        if not os.path.exists(output_path):
            os.mkdir(output_path)
            output_path += os.path.basename(os.path.dirname(input_path)) + ".nii"
            open(output_path, 'a').close()
        else:
            output_path += os.path.basename(os.path.dirname(input_path)) + ".nii"
            open(output_path, 'a').close()

        dicom_array = DICOMImporter.open_dicom_from_folder(input_path);
        #else:
        #    raise FileExistsError("You are trying to convert " +
        #        "an already converted scan, available at " +
        #       output_path)

        try:
            dicom2nifti.convert_dicom.dicom_array_to_nifti(dicom_array, output_path)
        #except AttributeError:
        except dicom2nifti.exceptions.ConversionError:
            print("Conversion Failed")

    #converts one dicom image into a nifti image
    def convert_dicom_single(input_path):
        folder = os.path.dirname(input_path)
        output_path = folder + "/nifti/0"
        if not os.path.exists(output_path):
            print("here")
        else:
            raise FileExistsError("You are trying to convert " +
                "an already converted scan, available at " +
                output_path)
        #try:
        dicom2nifti.dicom_series_to_nifti(input_path, output_path, reorient_nifti=True)
        #except dicom2nifti.exceptions.ConversionError:
        #    print("Conversion Failed")

class decompress_DICOM:
    def decompress_series(input_path):
        if not os.path.exists(os.path.dirname(input_path) + "/dicom_raw"):
            os.mkdir(os.path.dirname(input_path) + "/dicom_raw")
        for dicom_path in glob.glob(input_path + '/*.dcm'):
            os.system("gdcmconv --raw " + dicom_path + " " + os.path.dirname(os.path.dirname(dicom_path)) \
                        + "/dicom_raw/raw_" + os.path.basename(dicom_path))

    def decompress_single(dicom_path):
        os.system("gdcmconv --raw " + dicom_path + " " + os.path.dirname(dicom_path) \
                    + "/raw_" + os.path.basename(dicom_path))
