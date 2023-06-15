import 'assets/global.css';
import { Suspense } from 'react';
import '../node_modules/bootstrap/dist/css/bootstrap.min.css';
import 'react-toastify/dist/ReactToastify.css';
import { ToastContainer } from 'react-toastify';
import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import RequireAuth from 'api/RequireAuth';
import RedirectRoute from 'api/RedirectRoute';
import Home from 'pages/Home/Home';
import Navbar from 'components/Navbar/Navbar';
import SignIn from 'pages/auth/SignIn';
import SignUp from 'pages/auth/SignUp';
import Admin from 'pages/admin/Admin';
import AddUser from 'pages/admin/users/AddUser';
import EditUser from 'pages/admin/users/EditUser';
import ViewUser from 'pages/admin/users/ViewUser';
import EditCourse from 'pages/admin/courses/EditCourse';
import ViewCourse from 'pages/admin/courses/ViewCourse';
import User from 'pages/user/User';
import AddCourse from 'pages/admin/courses/AddCourse';
import AccountDetails from 'pages/user/AccountDetails';
import Footer from 'components/Footer/Footer';
import Courses from 'pages/Courses/Courses';
import CourseItemDetails from 'pages/Courses/CourseItemDetails';
import CourseItems from 'pages/Courses/CourseItems';
import { AuthProvider } from 'context/AuthContext';

function App() {
  return (
    <>
      <ToastContainer />
      <AuthProvider>
        <Suspense fallback='...is loading'>
          <div className='App min-vh-100'>
            <div id='app-root' />
            <BrowserRouter>
              <Navbar />

              <Routes>
                {/* Public routes */}
                <Route exact path='/' element={<Home />} />
                <Route exact path='*' element={<Navigate to='/' />} />
                <Route element={<RedirectRoute />}>
                  <Route exact path='/login' element={<SignIn />} />
                  <Route exact path='/register' element={<SignUp />} />
                </Route>

                {/* Authorized routes */}
                <Route
                  element={<RequireAuth allowedRoles={['USER', 'ADMIN']} />}>
                  <Route exact path='/courses' element={<Courses />} />
                  <Route
                    exact
                    path='/courses/:courseID'
                    element={<CourseItems />}
                  />
                  <Route
                    exact
                    path='/users/:userID/user-courses/:courseID'
                    element={<CourseItems />}
                  />
                  <Route
                    exact
                    path='/courses/:courseID/users/:userID'
                    element={<CourseItems />}
                  />
                  <Route
                    exact
                    path='/courses/:courseID/items/:itemID'
                    element={<CourseItemDetails />}
                  />
                  <Route exact path='/user' element={<User />} />
                  <Route
                    exact
                    path='/accountdetails'
                    element={<AccountDetails />}
                  />
                </Route>
                <Route element={<RequireAuth allowedRoles={'ADMIN'} />}>
                  <Route exact path='/admin' element={<Admin />} />
                  <Route exact path='/addcourse' element={<AddCourse />} />
                  <Route
                    exact
                    path='/editcourse/:id'
                    element={<EditCourse />}
                  />
                  <Route
                    exact
                    path='/viewcourse/:id'
                    element={<ViewCourse />}
                  />
                  <Route exact path='/adduser' element={<AddUser />} />
                  <Route exact path='/edituser/:id' element={<EditUser />} />
                  <Route exact path='/viewuser/:id' element={<ViewUser />} />
                </Route>
              </Routes>
              <Footer />
            </BrowserRouter>
          </div>
        </Suspense>
      </AuthProvider>
    </>
  );
}

export default App;
