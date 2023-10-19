//
//  NotificationViewController.m
//  NotificationContentDemo
//
//  Created by Karthik Iyer on 17/07/23.
//

#import "NotificationViewController.h"

@interface NotificationViewController () <UNNotificationContentExtension>

@property IBOutlet UILabel *label;

@end

@implementation NotificationViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any required interface initialization here.
}

- (void)didReceiveNotification:(UNNotification *)notification {
    self.label.text = notification.request.content.body;
}

@end
