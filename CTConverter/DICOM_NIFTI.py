import dicom2nifti #documentation at https://media.readthedocs.org/pdf/dicom2nifti/latest/dicom2nifti.pdf
import os
from DICOMImporter import DICOMImporter

class DICOM_2_NIFTI:
    #converts a full series of dicom images into a nifti image
    def convert_dicom_series(input_path):
        folder = os.path.dirname(input_path)
        output_path = folder + "/nifti"
        if not os.path.exists(output_path):
            os.mkdir(output_path)

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

DICOM_2_NIFTI.convert_dicom_series("./test_images/knee/dicom")
