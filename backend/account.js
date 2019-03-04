var db = require('./config/database');

var getUsername = userName => {
  return new Promise(function(resolve, reject) {
    db.query(
      'SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME LIKE ?',
      [userName],
      function(err, rows) {
        if (err) {
          reject(() => console.log(err + ''));
        }
        resolve(rows);
      }
    );
  });
};

var getEmail = email => {
  return new Promise(function(resolve, reject) {
    db.query('SELECT PHONE FROM USERS WHERE MAIL LIKE ?', [email], function(
      err,
      rows
    ) {
      if (err) {
        reject(() => console.log(err + ''));
      }
      resolve(rows);
    });
  });
};

var account = {
  checkExistUserName: async userName => {
    try {
      let result = await getUsername(userName);
      return result.length > 0 ? true : false;
    } catch (ex) {
      console.log(ex);
      return false;
    }
  },
  checkExistEmail: async email => {
    try {
      let result = await getEmail(email);
      return result.length > 0 ? true : false;
    } catch (ex) {
      console.log(ex);
      return false;
    }
  },
  login: function(userName, password, callback) {
    db.query(
      'SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME LIKE ? AND PASSWORD = ?',
      [userName, password],
      (err, rows) => {
        if (rows.length > 0) {
          db.query('CALL PROC_LOGIN_EVENT(?)', [userName]);
          return db.query('CALL PROC_GET_INFO_USER(?)', [userName], callback);
        } else
          return db.query(
            'SELECT USER_NAME FROM ACCOUNT WHERE USER_NAME LIKE ? AND PASSWORD = ?',
            [userName, password],
            callback
          );
      }
    );
  },
  logout: function(userName, callback) {
    return db.query('CALL PROC_LOGOUT_EVENT(?)', [userName], callback);
  },
  registerAccount: async function(userName, password, email, callback) {
    return db.query(
      'CALL PROC_INSERT_ACCOUNT(?, ?, ?);',
      [userName, password, email],
      callback
    );
  },
  changePassword: async (userName, password, callback) => {
    return db.query(
      'CALL PROC_CHANGE_PASSWORD_ACCOUNT (?, ?)',
      [password, userName],
      callback
    );
  },
  changeFullName: (userName, fullName, callback) => {
    return db.query(
      'CALL PROC_CHANGE_FULL_NAME(?, ?)',
      [userName, fullName],
      callback
    );
  },
  changeDoB: (userName, dob, callback) => {
    return db.query('CALL PROC_CHANGE_DOB(?, ?)', [userName, dob], callback);
  },
  changeGender: (userName, gender, callback) => {
    return db.query(
      'CALL PROC_CHANGE_GENDER(?, ?)',
      [userName, gender],
      callback
    );
  },
  changeMail: (userName, mail, callback) => {
    return db.query('CALL PROC_CHANGE_MAIL(?, ?)', [userName, mail], callback);
  },
  changePhone: (userName, phone, callback) => {
    return db.query(
      'CALL PROC_CHANGE_PHONE(?, ?)',
      [userName, phone],
      callback
    );
  },
  deleteAccount: function(userName, callback) {
    return db.query('CALL PROC_DELETE_ACCOUNT (?)', [userName], callback);
  },
};
module.exports = account;
