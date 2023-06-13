import { toast } from 'react-toastify';

export const TOAST_AUTOCLOSE_SHORT = 1000;
export const TOAST_AUTOCLOSE_LONG = 3000;
export const TOAST_SUCCESS = 'success';
export const TOAST_ERROR = 'error';

const CustomToast = (type, message, time) => {
  switch (type) {
    case 'success':
      toast.success(message, {
        autoClose: time,
      });
      break;
    case 'error':
      toast.error(message, {
        autoClose: time,
      });
      break;
    default:
      break;
  }
};

export default CustomToast;
