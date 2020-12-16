using Donation.BusinessLogic.Interface;
using Donation.Domain;
using Donation.DataAccess.Interface;
using System;

namespace Donation.BusinessLogic
{
    public class SessionLogic : ISessionLogic
    {
        private IUnitOfWork unitOfWork;
        public SessionLogic(IUnitOfWork _unitOfWork)
        {
            unitOfWork = _unitOfWork;
        }

        public Result<User> GetUserFromToken(Guid token)
        {
            bool sessionExist = unitOfWork.SessionRepository.Exist(s => s.Token == token);
            if (sessionExist)
            {
                Session session = unitOfWork.SessionRepository.Get(s => s.Token == token);
                return new Result<User>()
                {
                    ResultObject = unitOfWork.UserRepository.Get(x => x.Id == session.User.Id)
                };
            }
            else
            {
                return new Result<User>()
                {
                    Message = "Invalid Token."
                };
            }
        }

        public Result<Session> Login(string email, string password)
        {
            Guid sessionToken = Guid.NewGuid();
            Session session = new Session();
            bool existUser = unitOfWork.UserRepository.Exist(x => x.Email.ToLower() == email.ToLower());
            if (existUser)
            {
                User user = unitOfWork.UserRepository.Get(x => x.Email.ToLower() == email.ToLower());
                if (user.Password.Equals(password) || user.IsExternal)
                {
                    session.Token = sessionToken;
                    session.User = user;
                    session.CreateDate = DateTime.Now;
                    bool existSession = unitOfWork.SessionRepository.Exist(x => x.User.Equals(user));
                    if (existSession)
                    {
                        Session oldSession = unitOfWork.SessionRepository.Get(x => x.User.Equals(user));
                        unitOfWork.SessionRepository.Update(oldSession.Id, session);
                        unitOfWork.SessionRepository.Save();
                    }
                    else
                    {
                        unitOfWork.SessionRepository.Add(session);
                        unitOfWork.SessionRepository.Save();
                    }
                }
                else
                {
                    return new Result<Session>()
                    {
                        Message = "Mail or password is not valid."
                    };
                }
            }
            else
            {
                return new Result<Session>()
                {
                    Message = "Mail or password is not valid."
                };
            }
            return new Result<Session>()
            {
                ResultObject = session
            };
        }

        public Result<Session> LoginExternal(User user)
        {
            User _user;
            Guid sessionToken = Guid.NewGuid();
            Session session = new Session();
            bool existUser = unitOfWork.UserRepository.Exist(x => x.Email.ToLower() == user.Email.ToLower());
            if (!existUser)
            {
                unitOfWork.UserRepository.Add(user);
                unitOfWork.UserRepository.Save();
            }

            _user = unitOfWork.UserRepository.Get(x => x.Email.ToLower() == user.Email.ToLower());
            if (_user.IsExternal)
            {
                session.Token = sessionToken;
                session.User = user;
                session.CreateDate = DateTime.Now;
                bool existSession = unitOfWork.SessionRepository.Exist(x => x.User.Equals(user));
                if (existSession)
                {
                    Session oldSession = unitOfWork.SessionRepository.Get(x => x.User.Equals(user));
                    unitOfWork.SessionRepository.Update(oldSession.Id, session);
                    unitOfWork.SessionRepository.Save();
                }
                else
                {
                    unitOfWork.SessionRepository.Add(session);
                    unitOfWork.SessionRepository.Save();
                }
            }
            else
            {
                return new Result<Session>()
                {
                    Message = "User is not valid."
                };
            };

            return new Result<Session>()
            {
                ResultObject = session
            };
        }
        public bool IsValidToken(Guid token)
        {
            return unitOfWork.SessionRepository.Exist(s => s.Token == token);
        }

        public Result<Session> Logout(Guid token)
        {
            bool validToken = unitOfWork.SessionRepository.Exist(s => s.Token == token);
            if (validToken)
            {
                Session session = unitOfWork.SessionRepository.Get(x => x.Token == token);
                unitOfWork.SessionRepository.Delete(session.Id);
                unitOfWork.SessionRepository.Save();
                return new Result<Session>()
                {
                    ResultObject = new Session(),
                    Message = "The session " + token + "has been removed."
                };
            }
            else
            {
                return new Result<Session>()
                {
                    Message = "It is not possible to delete the session " + token + " because the token does not exist."
                };
            }
        }

    }
}