import React from 'react';
import LocalLibraryIcon from '@mui/icons-material/LocalLibrary';
import AccountCircleIcon from '@mui/icons-material/AccountCircle';
import LogoutIcon from '@mui/icons-material/Logout';
import { getUserRole } from 'api/AxiosHelper';
import { useAuth } from 'context/AuthContext';
import { useNavigate } from 'react-router-dom';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

const NavbarData = () => {
  const auth = useAuth();
  const navigate = useNavigate();
  const { t } = useTranslation();

  const logoutUser = async () => {
    localStorage.clear();
    auth.logout();
    navigate('/');
    CustomToast(TOAST_SUCCESS, t('auth.success.logout'), TOAST_AUTOCLOSE_SHORT);
  };

  const role = getUserRole();

  return [
    {
      title: 'login',
      icon: <AccountCircleIcon sx={{ width: 32, height: 32 }} />,
      link: '/login',
      roles: [],
    },
    {
      title: 'Courses',
      icon: <LocalLibraryIcon sx={{ width: 32, height: 32 }} />,
      link: '/courses',
      roles: ['USER', 'ADMIN'],
    },
    {
      title: 'Account',
      icon: <AccountCircleIcon sx={{ width: 32, height: 32 }} />,
      link: role === 'USER' ? '/user' : '/admin',
      roles: ['USER', 'ADMIN'],
    },
    {
      title: 'Logout',
      icon: <LogoutIcon sx={{ width: 32, height: 32 }} />,
      link: '/',
      roles: ['USER', 'ADMIN'],
      onClick: logoutUser,
    },
  ];
};

export default NavbarData;
