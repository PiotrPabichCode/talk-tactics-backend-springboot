import { Navigate, Outlet } from 'react-router-dom';
import { getUserRole } from './AxiosHelper';

const RedirectRoute = () => {
  const role = getUserRole();
  return role ? <Navigate to='/' /> : <Outlet />;
};

export default RedirectRoute;
