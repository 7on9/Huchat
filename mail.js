var nodemailer = require('nodemailer');

var transporter = nodemailer.createTransport({
    service: 'gmail',
    auth: {
        user: 'hutechgooflow2018@gmail.com',
        pass: '8ORqd1aaz776ktsLTwhf'
    }
});
var mailOptions = {
    from: 'hutechgooflow2018@gmail.com',
    to: 'EMAIL',
    subject: '[NOREPLY] HUCHAT RECOVERY PASS',
    text: 'That was easy!'
};

var fixMailOptions = (email) => {
    mailOptions.to = email;
};
var mail = {
    sendMail: async (email) => {
        await fixMailOptions(email);
        transporter.sendMail(mailOptions, function (error, info) {
            if (error) {
                console.log(error);
            } else {
                console.log('Email sent: ' + info.response);
            }
        });
    }
};

module.exports = mail;

