import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-components';
import {PLANET_LINK} from "@/constant";
const Footer: React.FC = () => {
  const defaultMessage= 'Scintilla出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'yhzx',
          title: '嘻精的用户中心',
          href: PLANET_LINK,
          blankTarget: true,
        },
        {
          key: 'Ant Design',
          title: 'Ant Design',
          href: 'https://ant.design',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <><GithubOutlined /> 嘻精 GitHub</>,
          href: 'https://github.com/ant-design/ant-design-pro',
          blankTarget: true,
        },

      ]}
    />
  );
};
export default Footer;
