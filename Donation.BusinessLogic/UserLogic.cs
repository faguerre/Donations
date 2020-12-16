using System;
using System.Collections.Generic;
using Donation.BusinessLogic.Interface;
using Donation.DataAccess.Interface;
using Donation.Domain;

namespace Donation.BusinessLogic
{
    public class UserLogic : IUserLogic
    {

        private IUnitOfWork unitOfWork;
        public UserLogic(IUnitOfWork _unitOfWork)
        {
            unitOfWork = _unitOfWork;
        }

        public Result<User> Add(User _user)
        {
            if (!unitOfWork.UserRepository.Exist(x => x.Email.Equals(_user.Email)))
            {
                unitOfWork.UserRepository.Add(_user);
                unitOfWork.UserRepository.Save();
                return new Result<User>()
                {
                    ResultObject = _user
                };
            }
            else
            {
                return new Result<User>()
                {
                    Message = "It is not possible to create an user because the Email is already used."
                };
            }
        }

        public Result<User> Delete(int id)
        {
            if (unitOfWork.UserRepository.Exist(x => x.Id == id))
            {
                unitOfWork.UserRepository.Delete(id);
                unitOfWork.UserRepository.Save();
                return new Result<User>()
                {
                    ResultObject = new User(),
                    Message = "The user " + id + "has been removed."
                };
            }
            else
            {
                return new Result<User>()
                {
                    Message = "It is not possible to delete the user " + id + " because the id does not exist."
                };
            }
        }

        public Result<User> Get(int id)
        {
            if (unitOfWork.UserRepository.Exist(x => x.Id == id))
            {
                return new Result<User>()
                {
                    ResultObject = unitOfWork.UserRepository.Get(x => x.Id == id)
                };
            }
            else
            {
                return new Result<User>()
                {
                    Message = "There is not user related to the id " + id
                };
            }
        }

        public IEnumerable<User> GetAll()
        {
            return unitOfWork.UserRepository.GetAll();
        }

        public Result<User> Update(int id, User _user)
        {
            if (unitOfWork.UserRepository.Exist(x => x.Id == id))
            {
                /*User userExist = unitOfWork.UserRepository.Get(x => x.Email.Equals(_user.Email));
                if (userExist != null)
                {
                    return new Result<User>()
                    {
                        Message = "It is not possible to update the user " + id + " because the new email has already been used."
                    };
                }*/
                unitOfWork.UserRepository.Update(id, _user);
                unitOfWork.UserRepository.Save();
                return new Result<User>()
                {
                    ResultObject = _user,
                    Message = "The user " + id + " has been updated."
                };
            }
            else
            {
                return new Result<User>()
                {
                    Message = "It is not possible to update the user " + id + " because the id does not exist."
                };
            }
        }

        public Result<UserTag> AddTag(Guid token, List<String> tags, List<String> userTags)
        {
            if (unitOfWork.SessionRepository.Exist(x => x.Token == token))
            {
                Session session = unitOfWork.SessionRepository.Get(x => x.Token == token);
                for (int i = 0; i < userTags.Count; i++)
                {
                    unitOfWork.UserTagRepository.Delete(session.User.Id);
                    unitOfWork.UserTagRepository.Save();
                }
                for (int i = 0; i < tags.Count; i++)
                {
                    UserTag userTag = new UserTag();
                    userTag.UserId = session.User.Id;
                    Tag tag = unitOfWork.TagRepository.Get(x => x.Name.Equals(tags[i]));
                    userTag.TagId = tag.Id;
                    unitOfWork.UserTagRepository.Add(userTag);
                    unitOfWork.UserTagRepository.Save();
                }

                return new Result<UserTag>()
                {
                    Message = "Tags add to user"
                };
            }
            else
            {
                return new Result<UserTag>()
                {
                    Message = "The User not exist."
                };
            }
        }
    }
}
