using System;
using Donation.Domain;
using System.ComponentModel.DataAnnotations;
using System.Collections.Generic;
using System.Text;

namespace Donation.WebApi
{
    public class DonationModel
    {
        [Required(ErrorMessage = "Name related to the donation is required")]
        public string Name {get; set;}
        
        [Required(ErrorMessage = "CreatorUser is required and should never be empty")]
        public int CreatorUserId {get; set;}

        [Required(ErrorMessage = "Description is required")]
        public string Description {get; set;}

        [Required(ErrorMessage = "Address is required and should never be empty")]
        public AddressModel Address {get; set;}
        public List<FileModel> FileList {get; set;}
        public List<String> Tags {get; set;}
        public DonationModel() {}
        internal Domain.Donation ToEntity()
        {
            return new Domain.Donation () {
                Name = this.Name,
                Description = this.Description,
                CreatorUser = new User() { Id = CreatorUserId},
                State = new State(),
                Address = this.Address.ToEntity(),
                Files = ConvertFiles(FileList)
            };
        }

        private List<File> ConvertFiles(List<FileModel> fileList)
        {
            List<File> files = new List<File>();
            if (fileList != null && fileList.Count != 0) 
            {
                for (int i = 0 ; i < fileList.Count ; i++)
                {
                    FileModel _fileModel = fileList[i];
                    Domain.File _file = new Domain.File() {
                        Name = _fileModel.Name,
                        DataFiles = System.Convert.FromBase64String(_fileModel.FileBase64)
                    };
                    files.Add(_file);
                }
            }
            return files;
        }
    }
}
