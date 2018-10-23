import dicom2nifti
import os

def convert_dicom_series(input_path):
    folder = os.path.dirname(input_path)
    output_path = folder + "/nifti"
    if not os.path.exists(output_path):
        os.mkdir(output_path)
    else:
        raise FileExistsError("You are trying to convert " +
            "an already converted scan, available at " +
            output_path)

    try:
        dicom2nifti.convert_directory(input_path, output_path)
    except dicom2nifti.exceptions.ConversionError:
        print("Conversion Failed")

def convert_dicom_single(input_path):
    folder = os.path.dirname(input_path)
    output_path = folder + "/nifti/0"
    if not os.path.exists(output_path):
        print("here")
    else:
        raise FileExistsError("You are trying to convert " +
            "an already converted scan, available at " +
            output_path)
    try:
        dicom2nifti.dicom_series_to_nifti(input_path, output_path, reorient_nifti=True)
    except dicom2nifti.exceptions.ConversionError:
        print("Conversion Failed")
