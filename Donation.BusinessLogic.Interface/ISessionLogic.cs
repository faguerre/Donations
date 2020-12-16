using System;
using Donation.Domain;

namespace Donation.BusinessLogic.Interface
{
    public interface ISessionLogic
    {
        Result<Session> Login(string email, string password);
        Result<Session> LoginExternal(User user);
        Result<User> GetUserFromToken(Guid token);
        bool IsValidToken(Guid token);
        Result<Session> Logout(Guid token);

    }
}