using System.Collections.Generic;
using System;

namespace Donation.WebApi
{
    public class UserTagModel
    {
        public Guid Token {get; set;}
        public List<String> Tags {get; set;}
        public List<String> UserTags {get; set;}
    }
}