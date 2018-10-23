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
    except BaseException:
        print("something went horribly wrong")
