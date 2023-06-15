import React, { useEffect, useState } from 'react';
import { getUsername, request } from 'api/AxiosHelper';
import { Edit } from '@mui/icons-material';
import { styled } from '@mui/material';
import CustomToast, {
  TOAST_AUTOCLOSE_SHORT,
  TOAST_ERROR,
  TOAST_SUCCESS,
} from 'components/CustomToast/CustomToast';
import { useTranslation } from 'react-i18next';

const AccountDetails = () => {
  const { t } = useTranslation();
  useEffect(() => {
    const username = getUsername();
    const loadUserDetails = async () => {
      try {
        const response = await request(
          'GET',
          `/api/users/by/login/${username}`
        );
        setUserID(response.data.id);
        setFirstName(response.data.firstName);
        setLastName(response.data.lastName);
        setEmail(response.data.email);
      } catch (error) {
        console.log(error);
      }
    };
    loadUserDetails();
  }, []);

  const [displayFirstName, setDisplayFirstName] = useState(false);
  const [displayLastName, setDisplayLastName] = useState(false);
  const [displayEmail, setDisplayEmail] = useState(false);
  const [displayPassword, setDisplayPassword] = useState(false);
  const [userID, setUserID] = useState(0);
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [newFirstName, setNewFirstName] = useState('');
  const [newLastName, setNewLastName] = useState('');
  const [newEmail, setNewEmail] = useState('');
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [repeatNewPassword, setRepeatNewPassword] = useState('');

  const handleEditClick = (event, field) => {
    event.preventDefault();
    switch (field) {
      case 'firstName':
        setNewFirstName(firstName);
        setDisplayFirstName(true);
        break;
      case 'lastName':
        setNewLastName(lastName);
        setDisplayLastName(true);
        break;
      case 'email':
        setNewEmail(email);
        setDisplayEmail(true);
        break;
      case 'password':
        setDisplayPassword(true);
        break;
      default:
        break;
    }
  };

  const resetNewFirstNameField = (event) => {
    event.preventDefault();
    setDisplayFirstName(false);
    setNewFirstName('');
  };

  const resetNewLastNameField = (event) => {
    event.preventDefault();
    setDisplayLastName(false);
    setNewLastName('');
  };

  const resetNewEmailField = (event) => {
    event.preventDefault();
    setDisplayEmail(false);
    setNewEmail('');
  };

  const resetNewPasswordFields = (event) => {
    event.preventDefault();
    setDisplayPassword(false);
    setOldPassword('');
    setNewPassword('');
    setRepeatNewPassword('');
  };

  const handleFirstNameForm = async (event) => {
    event.preventDefault();
    try {
      await request('PUT', `/api/users/${userID}/firstName`, newFirstName);
      setFirstName(newFirstName);
      CustomToast(
        TOAST_SUCCESS,
        t('toast.success.edit.first_name'),
        TOAST_AUTOCLOSE_SHORT
      );
      resetNewFirstNameField(event);
    } catch (error) {
      CustomToast(
        TOAST_ERROR,
        t('toast.error.edit.first_name'),
        TOAST_AUTOCLOSE_SHORT
      );
      console.log(error);
    }
  };

  const handleLastNameForm = async (event) => {
    event.preventDefault();
    try {
      await request('PUT', `/api/users/${userID}/lastName`, newLastName);
      setLastName(newLastName);
      CustomToast(
        TOAST_SUCCESS,
        t('toast.success.edit.last_name'),
        TOAST_AUTOCLOSE_SHORT
      );
      resetNewLastNameField(event);
    } catch (error) {
      CustomToast(
        TOAST_ERROR,
        t('toast.error.edit.last_name'),
        TOAST_AUTOCLOSE_SHORT
      );
      console.log(error);
    }
  };

  const handleEmailForm = async (event) => {
    event.preventDefault();
    try {
      await request('PUT', `/api/users/${userID}/email`, newEmail);
      setEmail(newEmail);
      CustomToast(
        TOAST_SUCCESS,
        t('toast.success.edit.email'),
        TOAST_AUTOCLOSE_SHORT
      );
      resetNewEmailField(event);
    } catch (error) {
      CustomToast(
        TOAST_ERROR,
        t('toast.error.edit.email'),
        TOAST_AUTOCLOSE_SHORT
      );
      console.log(error);
    }
  };

  const handlePasswordForm = async (event) => {
    event.preventDefault();
    const newPasswordObj = {
      currentPassword: '',
      oldPassword: oldPassword,
      newPassword: newPassword,
      repeatNewPassword: repeatNewPassword,
    };
    console.log(newPasswordObj);
    try {
      await request('PUT', `/api/users/${userID}/password`, newPasswordObj);
      setNewPassword(newPassword);
      CustomToast(
        TOAST_SUCCESS,
        t('toast.success.edit.password'),
        TOAST_AUTOCLOSE_SHORT
      );
      resetNewPasswordFields(event);
    } catch (error) {
      CustomToast(
        TOAST_ERROR,
        t('toast.error.edit.password'),
        TOAST_AUTOCLOSE_SHORT
      );
      console.log(error);
    }
  };

  const EditHoverIcon = styled(Edit)`
    &:hover {
      cursor: pointer;
      color: blue;
    }
  `;

  const renderFirstNameEdit = () => {
    return (
      <div className='col-sm-9'>
        <form onSubmit={(e) => handleFirstNameForm(e)}>
          <div className='row'>
            <div className='col-sm-9'>
              <input
                className='form-control'
                type='text'
                value={newFirstName}
                placeholder={t(
                  'user.account_details.form.first_name_placeholder'
                )}
                onChange={(e) => setNewFirstName(e.target.value)}
                required
              />
            </div>
            <div className='col-sm-1 me-3'>
              <button
                className='btn btn-danger'
                onClick={(e) => resetNewFirstNameField(e)}>
                {t('user.account_details.form.cancel')}
              </button>
            </div>
            <div className='col-sm-1'>
              <button className='btn btn-success' type='submit'>
                {t('user.account_details.form.submit')}
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  };

  const renderFirstNameField = () => {
    return (
      <div className='col-sm-9'>
        <p className='text-muted mb-0'>
          {firstName}
          <EditHoverIcon
            fontSize='small'
            onClick={(e) => handleEditClick(e, 'firstName')}
          />
        </p>
      </div>
    );
  };

  const renderLastNameEdit = () => {
    return (
      <div className='col-sm-9'>
        <form onSubmit={(e) => handleLastNameForm(e)}>
          <div className='row'>
            <div className='col-sm-9'>
              <input
                className='form-control'
                type='text'
                value={newLastName}
                placeholder={t(
                  'user.account_details.form.last_name_placeholder'
                )}
                onChange={(e) => setNewLastName(e.target.value)}
                required
              />
            </div>
            <div className='col-sm-1 me-3'>
              <button
                className='btn btn-danger'
                onClick={(e) => resetNewLastNameField(e)}>
                {t('user.account_details.form.cancel')}
              </button>
            </div>
            <div className='col-sm-1'>
              <button className='btn btn-success' type='submit'>
                {t('user.account_details.form.submit')}
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  };

  const renderLastNameField = () => {
    return (
      <div className='col-sm-9'>
        <p className='text-muted mb-0'>
          {lastName}
          <EditHoverIcon
            fontSize='small'
            onClick={(e) => handleEditClick(e, 'lastName')}
          />
        </p>
      </div>
    );
  };

  const renderEmailEdit = () => {
    return (
      <div className='col-sm-9'>
        <form onSubmit={(e) => handleEmailForm(e)}>
          <div className='row'>
            <div className='col-sm-9'>
              <input
                className='form-control'
                type='email'
                value={newEmail}
                placeholder={t('user.account_details.form.email_placeholder')}
                onChange={(e) => setNewEmail(e.target.value)}
                required
              />
            </div>
            <div className='col-sm-1 me-3'>
              <button
                className='btn btn-danger'
                onClick={(e) => resetNewEmailField(e)}>
                {t('user.account_details.form.cancel')}
              </button>
            </div>
            <div className='col-sm-1'>
              <button className='btn btn-success' type='submit'>
                {t('user.account_details.form.submit')}
              </button>
            </div>
          </div>
        </form>
      </div>
    );
  };

  const renderEmailField = () => {
    return (
      <div className='col-sm-9'>
        <p className='text-muted mb-0'>
          {email}
          <EditHoverIcon
            fontSize='small'
            onClick={(e) => handleEditClick(e, 'email')}
          />
        </p>
      </div>
    );
  };

  const renderPasswordEdit = () => {
    return (
      <div className='col-sm-9'>
        <form className='row' onSubmit={(e) => handlePasswordForm(e)}>
          <div className='col-sm-3'>
            <input
              className='form-control'
              type='password'
              value={oldPassword}
              placeholder={t('user.account_details.form.password_old')}
              onChange={(e) => setOldPassword(e.target.value)}
              required
            />
          </div>
          <div className='col-sm-3'>
            <input
              className='form-control'
              type='password'
              value={newPassword}
              placeholder={t('user.account_details.form.password_new')}
              onChange={(e) => setNewPassword(e.target.value)}
              required
            />
          </div>
          <div className='col-sm-3'>
            <input
              className='form-control'
              type='password'
              value={repeatNewPassword}
              placeholder={t('user.account_details.form.password_new_repeat')}
              onChange={(e) => setRepeatNewPassword(e.target.value)}
              required
            />
          </div>
          <div className='col-sm-1 me-3'>
            <button
              className='btn btn-danger'
              onClick={(e) => resetNewPasswordFields(e)}>
              {t('user.account_details.form.cancel')}
            </button>
          </div>
          <div className='col-sm-1'>
            <button className='btn btn-success' type='submit'>
              {t('user.account_details.form.submit')}
            </button>
          </div>
        </form>
      </div>
    );
  };

  const renderPasswordField = () => {
    return (
      <div className='col-sm-9'>
        <p className='text-muted mb-0'>
          *********
          <EditHoverIcon
            fontSize='small'
            onClick={(e) => handleEditClick(e, 'password')}
          />
        </p>
      </div>
    );
  };

  return (
    <>
      <div className='container'>
        <div className='py-4'>
          <div className='card mb-4'>
            <div className='card-header'>{t('user.account_details.title')}</div>
            <div className='card-body'>
              <div className='row align-items-center'>
                <div className='col-sm-3'>
                  <p className='mb-0'>{t('user.account_details.first_name')}</p>
                </div>
                {displayFirstName
                  ? renderFirstNameEdit()
                  : renderFirstNameField()}
              </div>
              <hr />
              <div className='row align-items-center'>
                <div className='col-sm-3'>
                  <p className='mb-0'>{t('user.account_details.last_name')}</p>
                </div>
                {displayLastName ? renderLastNameEdit() : renderLastNameField()}
              </div>
              <hr />
              <div className='row align-items-center'>
                <div className='col-sm-3'>
                  <p className='mb-0'>Email</p>
                </div>
                {displayEmail ? renderEmailEdit() : renderEmailField()}
              </div>
              <hr />
              <div className='row align-items-center'>
                <div className='col-sm-3'>
                  <p className='mb-0'>{t('user.account_details.password')}</p>
                </div>
                {displayPassword ? renderPasswordEdit() : renderPasswordField()}
              </div>
              <hr />
            </div>
          </div>
        </div>
      </div>
    </>
  );
};

export default AccountDetails;
