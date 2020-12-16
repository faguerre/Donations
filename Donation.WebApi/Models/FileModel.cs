using Donation.Domain;

namespace Donation.WebApi

{
    public class FileModel
    {
        public string Name { get; set; }
        public string FileBase64 { get; set; }
        public FileModel() { }
    }
}