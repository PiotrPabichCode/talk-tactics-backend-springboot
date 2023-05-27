import { Navigate, Outlet } from 'react-router-dom';
import { getUserRole } from './AxiosHelper';

const RequireAuth = ({ allowedRoles }) => {
  const role = getUserRole();

  return allowedRoles?.includes(role) ? <Outlet /> : <Navigate to='/login' />;
};

export default RequireAuth;
