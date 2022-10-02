import { CardData } from "./card-data";
import { Site } from "./site";
import { UserDetails } from "./user-details";

export class DashboardData {
    blackListedUrlsCard: CardData;
    usersCard: CardData;
    urlVerificationCard: CardData;
    phishingUrlsCard: CardData;
    userDetailsList: Array<UserDetails>;
    siteList: any;
    userUrlRequestList: any;
}
