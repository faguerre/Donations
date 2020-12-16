using System;
using System.Collections.Generic;
using Donation.Domain;

namespace Donation.BusinessLogic.Interface
{
     public interface IUserLogic
    {
        Result<User> Add(User user);
        Result<User> Update(int id, User user);
        Result<User> Delete(int id);
        Result<User> Get(int id);
        IEnumerable<User> GetAll();
        Result<UserTag> AddTag(Guid userToken, List<String> tags, List<String> userTags);
    }
}
