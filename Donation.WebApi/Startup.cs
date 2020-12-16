using Donation.BusinessLogic;
using Donation.BusinessLogic.Interface;
using Donation.DataAccess;
using Donation.DataAccess.Interface;
using Donation.Domain;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using Microsoft.Extensions.Hosting;


namespace Donation.WebApi
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddControllers();
            services.AddDbContext<DbContext, DonationContext>(o => o.UseSqlServer(
                @"Server=localhost\SQLEXPRESS;Database=DonationDB;Trusted_Connection=True; MultipleActiveResultSets=true").UseLazyLoadingProxies());
               /* @"Server=localhost\SQLEXPRESS;Database=DonationDB;
                Trusted_Connection=True; MultipleActiveResultSets=true").UseLazyLoadingProxies());*/
                
            services.AddScoped<IUserLogic, UserLogic>();
            services.AddScoped<IDonationLogic, DonationLogic>();
            services.AddScoped<IDonationEventLogic, DonationEventLogic>();
            services.AddScoped<IRepository<Domain.Donation>, DonationRepository>();
            services.AddScoped<IRepository<User>, UserRepository>();
            services.AddScoped<IUnitOfWork, UnitOfWork>();
            services.AddScoped<ISessionLogic, SessionLogic>();
            services.AddScoped<IRepository<Session>, SessionRepository>();
            services.AddScoped<ITagLogic, TagLogic>();
            services.AddScoped<IRepository<Tag>, TagRepository>();
            services.AddControllersWithViews()
                .AddNewtonsoftJson(options =>
                options.SerializerSettings.ReferenceLoopHandling = Newtonsoft.Json.ReferenceLoopHandling.Ignore
            );
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IWebHostEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseDeveloperExceptionPage();
            }

            app.UseHttpsRedirection();

            app.UseRouting();

            app.UseAuthorization();
            
            app.UseEndpoints(endpoints =>
            {
                endpoints.MapControllers();
            });
        }
    }
}
